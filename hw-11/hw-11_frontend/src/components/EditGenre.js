import React, {Component} from "react";
import {createGenre, updateGenre} from "../services/GenreService";

export class EditGenre extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            genre: {
                genreId: '',
                genreName: ''
            }
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({genre: nextProps.genre})
    }

    handleChange = (event) => {
        const name = event.target.name
        this.setState((prevState) => ({
            genre: {
                ...prevState.genre,
                [name]: event.target.value
            }
        }))
    }
    handleSubmit = (event) => {
        event.preventDefault()
        const genreName = this.state.genre.genreName
        if (genreName.length >= 2) {
            if (this.state.genre.id) {
                this.updateGenre()
            } else {
                this.createGenre()
            }
            document.querySelectorAll('[data-bs-dismiss=modal]')
                .forEach(elem => elem.click())
        }

    }
    updateGenre = () => {
        const genre = this.state.genre
        updateGenre(genre)
            .then(response => {
                this.setState({genre: response})
                this.props.onUpdate()
            })
    }
    createGenre = () => {
        const genre = this.state.genre
        createGenre(genre)
            .then(response => {
                this.setState({genre: response})
                this.props.onUpdate()
            })
    }

    render() {
        return (
            <div className="modal fade" id="editGenreModal" tabIndex="-1" aria-labelledby="genreModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="genreModalLabel">Edit genre</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form id="genreData" method="post" onSubmit={this.handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="inputGenreId1" className="form-label">Genre Id</label>
                                    <input type="number" className="form-control"
                                           id="inputGenreId1" value={this.state.genre.genreId || ''} disabled
                                           onChange={this.handleChange}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="genreName1" className="form-label">Genre Name</label>
                                    <input name="genreName" type="text" className="form-control"
                                           id="genreName1" value={this.state.genre.genreName || ''}
                                           onChange={this.handleChange}
                                           required/>
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