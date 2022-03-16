import {useParams} from "react-router-dom";
import {Book} from "./Book";

const BookDetails = () => {
    const {id} = useParams()

    return <Book bookId={id}/>
}
export default BookDetails