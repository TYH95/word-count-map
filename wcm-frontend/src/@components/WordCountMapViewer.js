
import styled from "@emotion/styled"
import { selectWordCountMap } from "@redux/wordCountMap"
import React from "react"
import { useSelector } from "react-redux"

const WordEntryWrapper = styled('div')(
    ({ theme }) => `
  display: flex;
  flex-direction: row;
  border: 1px solid black;
`,
);
const WordEntryKey = styled('div')(
    ({ theme }) => `
  flex: 100px 1 0;
  text-align: left;
`,
);

const WordEntryValue = styled('div')(
    ({ theme }) => `
  flex: 10% 0 0;
  text-align: right;
`,
);

const WordCountMapViewer = () => {
    const wordCountMap = useSelector(selectWordCountMap)


    return <>
        {Object.keys(wordCountMap).map((key, index) =>
            <WordEntryWrapper key={`wcm-w-${index}`}>
                <WordEntryKey key={`wcm-k-${index}`}>{key}</WordEntryKey>
                <WordEntryValue key={`wcm-v-${index}`}>{wordCountMap[key]}</WordEntryValue>
            </WordEntryWrapper>
        )
        }
    </>

}



export default WordCountMapViewer