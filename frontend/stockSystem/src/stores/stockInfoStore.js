import { defineStore } from 'pinia'

export const useStockInfoStore = defineStore('stockInfo', () => {
    return {
        stockId: '',
        setStockId(id) {
            this.stockId = id
        }
    }
})
