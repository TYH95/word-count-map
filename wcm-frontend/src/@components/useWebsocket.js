import { updateWCM } from "@redux/wordCountMap"
import { useEffect, useRef, useState } from "react"
import { useDispatch } from "react-redux"

export const STATUSES = {
    INIT: 'INIT',
    CONNECTING: 'CONNECTING',
    READY: 'READY',
    ERROR: 'ERROR',
    CLOSED: 'CLOSED'
}

const useWebSocket = (url) => {
    const dispatch = useDispatch()
    const [status, setStatus] = useState(STATUSES.INIT)
    const socket = useRef(null)

    console.log("Render useWebSocket")

    useEffect(() => {
        const handleMessage = (event) => {
            dispatch(updateWCM(event.data))
        }

        if (!socket.current) {
            setStatus(STATUSES.CONNECTING)
            socket.current = new WebSocket(url)
            socket.current.onmessage = (event) => handleMessage(event)
            socket.current.onopen = () => setStatus(STATUSES.READY)
        }

    }, [socket, url, dispatch])

    return status
}


export default useWebSocket