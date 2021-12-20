import React from 'react';
import {useState} from "react";
import './SearchBar.css'
import { useNavigate } from 'react-router-dom';

function SearchBar({setCurrentConstituency}) {

    const [searchTerm, setSearchTerm] = useState();
    const navigate = useNavigate();

    const handleSubmit = (event, postcode) => {
        event.preventDefault();
        fetch(
            `http://localhost:8080/api/constituencies/${postcode}`, {
                method: "GET",
                headers: {
                "Content-Type": "application/json"
                }
            })
            .then(response => {
                if(!response.ok){
                    return response.json().then(err => {throw new Error(err.message)})
                }
                return response.json()})
                .catch(err => {
                    console.log(err)
                    alert("Please enter a valid postcode");
                })
            .then(data =>{
                console.log(data)
                fetch("http://localhost:8080/api/constituencies/nomp")
                .then(response => response.json())
                .then(response => {
                    if (response.includes(data.constituency_name)){
                        alert("This constituency currently has no MP. Functionality will return once it has a new one, so check back soon")
                    }
                    else{
                setCurrentConstituency(data)
                navigate('/constituency/current')
            }
        })})}
    

    return (
        <div className='search-bar'>
            <form onSubmit={(event) => handleSubmit(event, searchTerm)}>
            <input className='constituency-search' type="text" placeholder="Find your constituency by postcode" 
            onChange={(event) => setSearchTerm(event.target.value)}></input>
            <button className='search-button' type="submit">Search</button>
            </form>
        </div>
    )
    }

export default SearchBar;
