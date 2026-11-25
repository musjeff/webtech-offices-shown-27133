import React from 'react'

function LibraryCards({bookInfo}) {
  return (
    <div style={{border:"10px solid blue",marginTop:"40px"}}>
      <div>{bookInfo.code}</div>
      <div>{bookInfo.name}</div>
      <div>{bookInfo.price}</div>
    </div>
  )
}

export default LibraryCards
