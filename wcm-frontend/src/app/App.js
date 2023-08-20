import "./App.css";
import useWebSocket, { STATUSES } from "@components/useWebsocket";
import WordCountMapViewer from "@components/WordCountMapViewer";
import styled from "@emotion/styled";
import { Button, CircularProgress } from "@mui/material";
import { useEffect, useState } from "react";

const WEBSOCKET_ENDPOINT = 'ws:127.0.0.1:8080/ws'

const Heading = styled('h1')({
  color: 'darkslategray',
  backgroundColor: 'aliceblue',
  padding: 8,
  borderRadius: 4,
  margin: 'auto',
});

const ContentWrapper = styled('div')({
  padding: 8,
});

function App() {
  const [retry, setRetry] = useState(false)
  const connectionStatus = useWebSocket(WEBSOCKET_ENDPOINT, retry)

  useEffect(() => {
    if (connectionStatus === STATUSES.CLOSED) {
      setRetry(false)
    }
  }, [connectionStatus])

  return (
    <div className="App">
      <Heading>WordCountMap Explorer</Heading>
      <ContentWrapper>
        {connectionStatus === STATUSES.READY && <WordCountMapViewer></WordCountMapViewer>}
        {connectionStatus === STATUSES.CONNECTING && <CircularProgress></CircularProgress>}
        {connectionStatus === STATUSES.CLOSED && <Button onClick={() => setRetry(true)}>Retry</Button>}
      </ContentWrapper>
    </div>
  );
}

export default App;
