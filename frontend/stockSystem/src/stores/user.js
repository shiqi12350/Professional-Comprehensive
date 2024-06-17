import {defineStore} from 'pinia'

const userInfoStore = defineStore('user',{
    state: () => ({
        userID: '',
      }),

      actions: {  
        reset() {  
            this.userID = '';  
        },  
    },  
    
})

export default userInfoStore;