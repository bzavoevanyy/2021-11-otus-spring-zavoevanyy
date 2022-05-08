export async function getAllCommentsByBookId(data) {
    const response = await fetch('/api/comment?' + new URLSearchParams({'bookId': data}));
    return await response.json();
}
export async function createComment(data) {
    const response = await fetch(`/api/comment`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function updateComment(data) {
    const response = await fetch(`/api/comment`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}
export async function deleteComment(data) {
    const response = await fetch(`/api/comment/` + data, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'}
    })
    return await response.json();
}