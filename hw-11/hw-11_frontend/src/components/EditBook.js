import React, {Component} from "react";
import {createBook, updateBook} from "../services/BookService";
export class EditBook extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            book : {
                id: '',
                title: '',
                authorId: '',
                authorName: '',
                genreId: '',
                genreName: ''
            }
        }

    }
    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.book) {
            this.setState({book: nextProps.book})
        }
    }
    handleChange = (event) => {
        const name = event.target.name;
        const authorName = name === 'authorId' ? this.props.authors.find(author => author.id === event.target.value).authorName : this.state.book.authorName
        const genreName = name === 'genreId' ? this.props.genres.find(genre => genre.id === event.target.value).genreName : this.state.book.genreName
        this.setState((prevState) => ({
            book : {...prevState.book,
            [name]: event.target.value,
            authorName: authorName,
            genreName: genreName}
        }))
    }
    handleSubmit = (event) => {
        event.preventDefault()
        const bookTitle = this.state.book.title
        if (bookTitle.length >=2) {
            if (this.state.book.id) {
                this.updateBook()
            } else {
                this.createBook()
            }
            document.querySelectorAll('[data-bs-dismiss=modal]')
                .forEach(elem => elem.click())
        }
    }
    updateBook = ()=> {
        const book = this.state.book
        updateBook(book)
            .then(response => {
                this.setState({book: response})
                this.props.onUpdate()
            })
    }
    createBook = () => {
        const book = this.state.book
        createBook(book)
            .then(response => {
                this.setState({book: response})
                this.props.onUpdate()
            })
    }
    render() {
        const authors = this.props.authors
        const genres = this.props.genres
        return (
            <div className="modal fade" id="editBookModal" tabIndex="-1" aria-labelledby="exampleModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Edit book</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form id="bookData" method="post" onSubmit={this.handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="inputId1" className="form-label">Book Id</label>
                                    <input name="id" type="number" className="form-control"
                                           id="inputId1" value={this.state.book.id || ''} onChange={this.handleChange} disabled/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="bookTitle1" className="form-label">Title</label>
                                    <input name="title" type="text" className="form-control"
                                           id="bookTitle1" required value={this.state.book.title || ''} onChange={this.handleChange}/>
                                </div>
                                <div className="input-group mb-3">
                                    <select name="authorId" className="form-select"
                                            id="inputAuthor1" required value={this.state.book.authorId || '0'}
                                            onChange={this.handleChange}>
                                        {authors && authors.map((author) => {
                                            return (
                                                <option key={author.id} value={author.id}>
                                                    {author.authorName}
                                                </option>
                                            )
                                        })}
                                    </select>
                                    <label className="input-group-text" htmlFor="inputAuthor1">Author</label>

                                </div>
                                <div className="input-group mb-3">
                                    <select name="genreId" className="form-select"
                                            id="inputGenre1" required value={this.state.book.genreId || ''} onChange={this.handleChange}>
                                        {genres && genres.map((genre) => {
                                            return (
                                                <option key={genre.id} value={genre.id}>
                                                    {genre.genreName}
                                                </option>
                                            )
                                        })}
                                    </select>
                                    <label className="input-group-text" htmlFor="inputGenre1">Genre</label>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close
                                    </button>
                                    <button type="submit" className="btn btn-primary">Save changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}