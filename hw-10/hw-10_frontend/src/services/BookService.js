export async function getAllBooks() {
    const response = await fetch('/book');
    return await response.json();
}
export async function createBook(data) {
    const response = await fetch(`/book`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function updateBook(data) {
    const response = await fetch(`/book/` + data.id, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function deleteBook(data) {
    const response = await fetch(`/book/` + data, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'}
    })
    return await response.json();
}
export async function getBookById(data) {
    const response = await fetch('/book/' + data);
    return await response.json();
}