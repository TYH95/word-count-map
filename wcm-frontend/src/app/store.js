import { configureStore } from "@reduxjs/toolkit";
import wordCountMapReducer from "@redux/wordCountMap"

export default configureStore({
  reducer: {
      wordCountMap: wordCountMapReducer
  },
});
