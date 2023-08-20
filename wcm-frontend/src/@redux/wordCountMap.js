import { createSlice } from '@reduxjs/toolkit'

export const STATUSES = {
    INIT: 'INIT',
    CONNECTING: 'CONNECTING',
    READY: 'READY',
    ERROR: 'ERROR',
}


export const wordCountMapSlice = createSlice({
    name: 'wordCountMap',
    initialState: {
        currentMap: {},
    },
    reducers: {
        update: (state, { payload }) => ({
            ...state,
            currentMap: payload,
        }),
    },
})

export const updateWCM = (wordCountMapRaw) => (dispatch, getState) => {
    const parsedMap = JSON.parse(wordCountMapRaw)
    const currentMap = getState().wordCountMap.currentMap
    //Check if current Map isEmpty
    if(Object.keys(currentMap)) {
        dispatch(update(parsedMap))
    }
}   

export const selectWordCountMap = state => state.wordCountMap.currentMap


export const { update } = wordCountMapSlice.actions
export default wordCountMapSlice.reducer
