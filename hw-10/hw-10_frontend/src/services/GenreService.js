export async function getAllGenres() {
    const response = await fetch('/genre');
    return await response.json();
}
export async function createGenre(data) {
    const response = await fetch(`/genre`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function updateGenre(data) {
    const response = await fetch(`/genre/` + data.genreId, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function deleteGenre(data) {
    const response = await fetch(`/genre/` + data, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'}
    })
    return await response.json();
}
