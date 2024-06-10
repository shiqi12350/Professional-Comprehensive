from flask import Flask, json, jsonify, request
import torch
import torch.nn as nn
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
import seaborn as sns
import numpy as np
#from LSTM import LSTM

app = Flask(__name__)

class LSTM(nn.Module):
    def __init__(self, input_dim, hidden_dim, num_layers, output_dim):
        super(LSTM, self).__init__()
        self.hidden_dim = hidden_dim
        self.num_layers = num_layers

        self.lstm = nn.LSTM(input_dim, hidden_dim, num_layers, batch_first=True)
        self.fc = nn.Linear(hidden_dim, output_dim)

    def forward(self, x):
        h0 = torch.zeros(self.num_layers, x.size(0), self.hidden_dim).requires_grad_()
        c0 = torch.zeros(self.num_layers, x.size(0), self.hidden_dim).requires_grad_()
        out, (hn, cn) = self.lstm(x, (h0.detach(), c0.detach()))
        out = self.fc(out[:, -1, :])
        return out

@app.route('/LSTM_prediction',methods=['POST'])
def LSTM_prediction():  # put application's code here
    data = request.get_json()

    # 检查是否有名为 'param1' 和 'param2' 的参数
    if 'model_name' in data and 'lookback' in data and 'future_days' in data :
        if(data['model_name']==1):
            model_name='lstm_110003.pth'
        if(data['model_name']==2):
            model_name='lstm_162201.pth'
        if(data['model_name']==3):
            model_name='lstm_217003.pth'

        lookback = data['lookback']
        future_days= data['future_days']

        # 检查是否有名为 'array' 的参数
        if 'array' in data:
            # 获取数组参数
            array_data = data['array']
            if(len(array_data)<lookback):
                return jsonify({'error': 'lookback oversize'}), 400

            # 将数组转换为 DataFrame
            df = pd.DataFrame(array_data, columns=['Close'])
            
        else:
            # 如果缺少数组参数，返回错误响应
            return jsonify({'error': 'Missing array parameter'}), 400
    else:
        # 如果缺少其他参数，返回错误响应
        return jsonify({'error': 'Missing parameter'}), 400


    data = df
    predict_data = data['Close']
    show_data=pd.DataFrame(predict_data)
    #print(show_data)

    # 规范化数据
    scaler = MinMaxScaler(feature_range=(-1, 1))
    predict_data_normalized = scaler.fit_transform(predict_data.values.reshape(-1, 1))

    def prepare_continuous_data(data, model, lookback, future_days):
        # 将最后 lookback 天的数据作为初始输入
        initial_input = data[-lookback:].reshape(1, lookback, 1)  # 添加额外的维度以匹配模型的预期输入形状
        initial_input_tensor = torch.tensor(initial_input, dtype=torch.float32)  # 转换为 PyTorch 张量
        inputs = torch.tensor(initial_input, dtype=torch.float32)  # 转换为 PyTorch 张量
        #pre=[]
        # 迭代预测未来 future_days 天的数据
        for _ in range(future_days):
            # 使用模型进行预测
            with torch.no_grad():
                prediction = model(inputs)  # 在第0维度添加一个维度作为批次维度
            # 将预测结果添加到输入中
            inputs = torch.cat([inputs[:, 1:], prediction.unsqueeze(1)], dim=1)  # 移除第一个时间步的数据，并添加预测结果
            initial_input_tensor = torch.cat([initial_input_tensor, prediction.unsqueeze(1)], dim=1)  # 将预测结果添加到初始输入中
        return initial_input_tensor[:, -future_days:, :]


    output_set_size = int(np.round(0.2 * data.shape[0]))
    input_set_size = data.shape[0] - (output_set_size)

    # 输入的维度为1，只有Close收盘价
    input_dim = 1
    # 隐藏层特征的维度
    hidden_dim = 32
    # 循环的layers
    num_layers = 2
    # 预测后一天的收盘价
    output_dim = 1

    # 加载模型
    model = LSTM(input_dim=input_dim, hidden_dim=hidden_dim, output_dim=output_dim, num_layers=num_layers)
    model.load_state_dict(torch.load(model_name))
    model.eval()  # 将模型设为评估模式

    continuous_inputs = prepare_continuous_data(predict_data_normalized[:-future_days],model,lookback, future_days)
    
    future_prediction = pd.DataFrame(scaler.inverse_transform(continuous_inputs[:, :, 0].detach().numpy()))

    future_prediction_transposed = future_prediction.T
    # 打印预测结果
    print("Future prediction:", future_prediction_transposed)
    
    pre_list = future_prediction_transposed.values.tolist()

    return jsonify({'Prediction': pre_list})


if __name__ == '__main__':
    app.run('0.0.0.0',port=14106)
