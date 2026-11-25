import React from 'react'
import LibraryCards from './LibraryCards'



function LibraryBooksList() {
    const books = [
        {
            code: 'WEB101',
            name: "web technology",
            price: 1000,
        },
        {
            code: 'SOD102',
            name: "software development",
            price: 2000,
        },
        {
            code: 'JS103',
            name: "javascript for beginners",
            price: 3000,
        }
    ]

    return (
        <div>
            <h2>list of books</h2>
            <ul>
                {
                books.map((book) => <LibraryCards bookInfo={book}/>)
                }
                
            </ul>

            <div>
                the total price for {books.length} books is {books.reduce((totalPrice, book) => totalPrice + book.price, 0)}
            </div>
        </div>
    )
}

export default LibraryBooksList