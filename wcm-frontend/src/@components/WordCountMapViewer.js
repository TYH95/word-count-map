
import { Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TextField } from "@mui/material";
import { selectWordCountMap } from "@redux/wordCountMap"
import React, { useEffect, useState } from "react"
import { useSelector } from "react-redux"
import WordCountMapTable from "./WordCountMapTable";



const WordCountMapViewer = () => {
    const wordCountMap = useSelector(selectWordCountMap)
    const [mapToDisplay, setMapToDisplay] = useState({})
    const [search, setSearch] = useState("")
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    useEffect(() => {
        setMapToDisplay(wordCountMap)
    }, [wordCountMap])

    useEffect(() => {
        const handleSearch = () => {
            const filteredMap = {}
            Object.keys(wordCountMap).filter(key => key.includes(search)).forEach(k => filteredMap[k] = wordCountMap[k])
            setMapToDisplay(filteredMap)
        }

        handleSearch(search)
    }, [search, wordCountMap])


    return <>
        <TextField label="Search" variant="outlined" onChange={(e) => setSearch(e.target.value)} />
        <TableContainer>
            <Table stickyHeader aria-label="sticky table">
                <TableHead>
                    <TableRow>
                        <TableCell align="left" colSpan={1}>
                            Word
                        </TableCell>
                        <TableCell align="right" colSpan={1}>
                            Occurance
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    <WordCountMapTable mapToDisplay={mapToDisplay} page={page} rowsPerPage={rowsPerPage} ></WordCountMapTable>
                </TableBody>
            </Table >
        </TableContainer >
        <TablePagination
            rowsPerPageOptions={[10, 25, 100]}
            component="div"
            count={Object.keys(mapToDisplay).length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
        />
    </>

}



export default WordCountMapViewer