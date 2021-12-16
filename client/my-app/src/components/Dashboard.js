import { useState, useEffect } from "react"
import CommentsList from "./comments/CommentsList"

const Dashboard = ({ token }) => {

    const [user, setUser] = useState(null)

    useEffect(() => {
        fetch(`http://localhost:8080/api/users/${token.userId}`,
        {
            method: 'POST',
            headers: {
                "content-type": "application/json"
            },
            body: JSON.stringify(token)
        })
        .then(response => response.json())
        .then(data => setUser(data))
    }, [token])

    return (
        <>
            {!user ?
            <p>Loading...</p> 
            :
            <section>
                <header>
                    <h1 className="profile-header">
                        Welcome 
                        {' ' + user.firstName.charAt(0).toUpperCase() + user.firstName.slice(1) + ' '} 
                        {user.lastName.charAt(0).toUpperCase() + user.lastName.slice(1)}
                    </h1>
                </header>
                <main className="profile-main-card">
                    <section className="profile-user-card">
                        <img className="profile-user-img" 
                            src={`https://avatars.dicebear.com/api/human/${user.firstName+user.lastName}.svg?size=200`} alt="User avatar" />
                        <p>Email: {user.email}</p>
                        <p>Constituency: {user.constituencyName}</p>
                    </section>
                    <section className="profile-user-comment-card">
                        <CommentsList comments={user.commentList}/>
                    </section>
                </main>
            </section>
            }
        </>
    )
}

export default Dashboard