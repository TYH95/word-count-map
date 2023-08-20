import React, { memo } from "react"
import { TableCell, TableRow } from "@mui/material";

const WordCountMapTable = memo(function WordCountMapTable({ mapToDisplay, page, rowsPerPage = 10 }) {
    return <>
        {
            Object.keys(mapToDisplay).slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((key, index) =>
                    <TableRow hover role="checkbox" tabIndex={-1} key={`wcm-w-${index}`}>
                        <TableCell key={`wcm-k-${index}`} align={'left'}>
                            {key}
                        </TableCell>
                        <TableCell key={`wcm-v-${index}`} align={'right'}>
                            {mapToDisplay[key]}
                        </TableCell>
                    </TableRow>
                )
        }
    </>
})

export default WordCountMapTable