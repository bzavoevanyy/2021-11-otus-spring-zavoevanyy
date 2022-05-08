export async function getAllGenres() {
    const response = await fetch('/api/genre');
    return await response.json();
}
export async function createGenre(data) {
    const response = await fetch(`/api/genre`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function updateGenre(data) {
    const response = await fetch(`/api/genre/`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function deleteGenre(data) {
    const response = await fetch(`/api/genre/` + data, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'}
    })
    return await response.json();
}
