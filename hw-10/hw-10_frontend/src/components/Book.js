import React, {Component} from "react";
import {getAllBooks, getBookById} from "../services/BookService";
import {getAllAuthor} from "../services/AuthorService";
import {getAllGenres} from "../services/GenreService";
import {Comment} from "./Comment";
import {EditBook} from "./EditBook";
import DeleteModal from "./DeleteModal";
import {Link} from "react-router-dom";

export class Book extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            books: [],
            authors: [],
            genres: [],
            chosenBook: '',
            entity: '',
            bookId: ''
        }
    }

    componentDidMount() {
        if (this.props.bookId) {
            this.getBookById()
        } else {
            this.getAllBooks()
        }
        this.getAllAuthors()
        this.getAllGenres()
    }

    getBookById = () => {
        getBookById(this.props.bookId).then(data => {
            this.setState({books: [data]})
        })
    }
    getAllAuthors = () => {
        getAllAuthor().then(data => {
            this.setState({authors: data})
        })
    }
    getAllBooks = () => {
        getAllBooks().then(data => {
            this.setState({books: data})
        })
    }
    getAllGenres = () => {
        getAllGenres().then(data => {
            this.setState({genres: data})
        })
    }
    handleClick = (e) => {
        const books = this.state.books;
        const nameOp = e.target.name
        if (nameOp === 'delete') {
            this.setState({entity: 'book'})
        }
        const chosenBook = books.find(book => book.id === parseInt(e.target.dataset.bookId))
        this.setState({
            chosenBook: chosenBook || {
                title: '',
                authorId: 1,
                genreId: 1
            }
        })
    }

    onUpdateBooksList = (fromSingleDelete) => {
        if (this.props.bookId && !fromSingleDelete) {
            this.getBookById()
        } else {
            this.getAllBooks()
        }
    }

    render() {
        const books = this.state.books
        return (
            <div>
                <table className="table">
                    <thead>
                    <tr>
                        <th scope="col">Book id</th>
                        <th scope="col">Title</th>
                        <th scope="col">Author</th>
                        <th scope="col">Genre</th>
                    </tr>
                    </thead>
                    <tbody>
                    {books && books.map((book) => {
                        return (
                            <tr key={book.id}>
                                <th scope="row">{book.id}</th>
                                <td><Link to={"/book/" + book.id} className="link-primary">{book.title}</Link></td>
                                <td>{book.authorName}</td>
                                <td>{book.genreName}</td>
                                <td>
                                    <button onClick={this.handleClick} type="button" className="btn btn-primary"
                                            data-bs-toggle="modal"
                                            data-book-id={book.id} data-bs-target="#editBookModal">
                                        Edit
                                    </button>
                                    <button onClick={this.handleClick} name="delete" type="button"
                                            className="btn btn-primary"
                                            data-bs-toggle="modal" data-book-id={book.id}
                                            data-bs-target="#deleteModal">
                                        Delete
                                    </button>
                                </td>
                            </tr>)
                    })}
                    </tbody>
                </table>
                {this.props.bookId ? '' :
                    <button name="edit" type="button" className="btn btn-primary btn-lg" data-bs-toggle="modal"
                            data-bs-target="#editBookModal" data-book-id="0" onClick={this.handleClick}>
                        Add book
                    </button>}
                {!this.props.bookId ? '' :
                <Comment bookId={this.props.bookId}/>}
                <EditBook
                    onUpdate={this.onUpdateBooksList}
                    book={this.state.chosenBook}
                    genres={this.state.genres}
                    authors={this.state.authors}/>
                <DeleteModal
                    entity={this.state.entity}
                    entityId={this.state.chosenBook.id}
                    onUpdate={this.onUpdateBooksList}/>
            </div>
        )
    }
}