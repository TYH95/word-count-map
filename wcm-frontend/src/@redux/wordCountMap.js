import { createSlice } from '@reduxjs/toolkit'

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

export const updateWCM = (wordCountMapRaw) => (dispatch) => {
    const parsedMap = JSON.parse(wordCountMapRaw)
    dispatch(update(parsedMap))
}

export const selectWordCountMap = state => state.wordCountMap.currentMap


export const { update } = wordCountMapSlice.actions
export default wordCountMapSlice.reducer
