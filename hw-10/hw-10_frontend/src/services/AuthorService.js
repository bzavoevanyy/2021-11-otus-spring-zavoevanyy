export async function getAllAuthor() {
    const response = await fetch('/author');
    return await response.json();
}
export async function createAuthor(data) {
    const response = await fetch(`/author`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function updateAuthor(data) {
    const response = await fetch(`/author/` + data.authorId, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function deleteAuthor(data) {
    const response = await fetch(`/author/` + data, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'}
    })
    return await response.json();
}