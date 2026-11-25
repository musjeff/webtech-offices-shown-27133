import React from 'react'

function LibraryHeader() {
    const libraryName = "library management system"
    return (
        <div>
            <header>
                <h1>{libraryName}</h1>
                <p>this is a library system that will help you to manage the books</p>
            </header>
        </div>
    )
}

export default LibraryHeader