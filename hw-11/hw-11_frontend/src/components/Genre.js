import React, {Component} from "react";
import {EditGenre} from "./EditGenre";
import DeleteModal from "./DeleteModal";
import {getAllGenres} from "../services/GenreService";

export class Genre extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            genres: '',
            chosenGenre: '',
            entity: ''
        }
    }

    componentDidMount() {
        this.getAllGenres()
    }

    getAllGenres = () => {
        getAllGenres().then(data => {
            console.log(data)
            this.setState({genres: data})
        })
    }
    onUpdateGenresList = () => {
        this.getAllGenres()
    }
    handleClick = (event) => {
        const genres = this.state.genres
        const nameOp = event.target.name
        const chosenGenre = genres.find(genre => genre.id === event.target.dataset.genreId)
        this.setState({chosenGenre: chosenGenre || ''})
        if (nameOp === 'delete') {
            this.setState({entity: 'genre'})
        }
    }

    render() {
        const genres = this.state.genres
        return (
            <div>
                <h3>Список жанров</h3>
                <table className="table">
                    <thead>
                    <tr>
                        <th scope="col">Genre name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {genres && genres.map((genre) => {
                        return (
                            <tr key={genre.id}>
                                <td>{genre.genreName}</td>
                                <td>
                                    <button name="delete" onClick={this.handleClick} type="button"
                                            className="btn btn-primary"
                                            data-bs-toggle="modal" data-genre-id={genre.id}
                                            data-bs-target="#deleteModal">Delete
                                    </button>
                                </td>
                            </tr>)
                    })}
                    </tbody>
                </table>
                <button name="edit" type="button" className="btn btn-primary btn-lg" data-bs-toggle="modal"
                        data-bs-target="#editGenreModal" data-genre-id="0" onClick={this.handleClick}>
                    Add genre
                </button>
                <EditGenre
                    onUpdate={this.onUpdateGenresList}
                    genre={this.state.chosenGenre}
                />
                <DeleteModal
                    entity={this.state.entity}
                    entityId={this.state.chosenGenre.id}
                    onUpdate={this.onUpdateGenresList}/>

            </div>
        )
    }
}