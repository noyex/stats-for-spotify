import React from "react";

const EditButton = () => {
    const handleEdit = () => {
        window.location.href = '/api/edit';
    };
    return <button onClick={handleEdit}>Edit</button>
}