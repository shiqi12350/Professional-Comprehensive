import {defineStore} from 'pinia'

const userInfoStore = defineStore('user',{
    state: () => ({
        userID: '',
      })
    
})

export default userInfoStore;