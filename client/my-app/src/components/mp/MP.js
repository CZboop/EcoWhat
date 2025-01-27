import VotesList from './VotesList';
import './MP.css'
import "../../App.css"

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTwitter } from '@fortawesome/free-brands-svg-icons'
import { faEnvelope } from '@fortawesome/free-solid-svg-icons'
import {useState, useEffect} from "react";

const MP = ({ mpData, mpVotes, user, token, contacted, setContacted }) => {

    const tweetText = "Please care more about the environment!";
    const emailText = `Dear ${mpData.name}, I am a constituent concerned about the environment, please help, From ${user==null?"your costituent": user.firstName + " " + user.latName}`;
    const [open, setOpen] = useState(false);

    const evaluateLastContact = () => {
        if (token){
        fetch("http://localhost:8080/api/users/lastcontact/" + token.userId)
        .then(response => response.text())
        .then(data => {
            const lastContact = new Date(data.toString());
            const now = new Date();
            const currentDate = now.getFullYear()+'/'+(now.getMonth()+1)+'/'+now.getDate() + " " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds()
            const diffTime = new Date(currentDate.toString()) - lastContact;
            const diffDays = Math.ceil(diffTime / (1000 * 3600 * 24))
            return diffDays;            
        })
        // timeout is 3 days here, can always increase or decrease this
        .then(days => days > 3 ? setContacted(false) : setContacted(true))
    }
        
    }

    useEffect(() => {
        evaluateLastContact();
    }, [])

    const handleContactClick = () => {
        setContacted(true);
        const today = new Date();
        fetch("http://localhost:8080/api/users/contacted/" + token.userId, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: today.getFullYear()+'/'+(today.getMonth()+1)+'/'+today.getDate() + " " + today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds()
        })
            
    }

    return (
        <section className="mp-section">

            <section className="mp-info">
                <header className="mp-info__header">
                    <h1 className="mp-name">{mpData.name}</h1>
                    <span className="mp__img-container">
                    <img className="mp__img" src={mpData.photoLink} alt="MP"/>
                    </span>
                    <h2>Party: {mpData.party}</h2>
                    <h2>Constituency: {mpData.constituencyName}</h2>
                    {token && contacted==false?
                    <>
                    <p>{mpData.twitter===""? "This MP doesn't have a Twitter on record. Click to tweet Downing Street instead!": ""}</p>
                    <footer className="mp-contact--icon">
                        <a className="mp-twitter--icon" href={mpData.twitter===""? `https://twitter.com/intent/tweet?text=@10DowningStreet%20${tweetText}`
                            :`https://twitter.com/intent/tweet?text=@${mpData.twitter.split('twitter.com/').at(-1)}%20${tweetText}`}>
                            <FontAwesomeIcon icon={faTwitter} onClick={() => handleContactClick()}/>
                        </a>
                            
                        <a className="mp-mail--icon" href={`mailto:${mpData.emailAddress}?subject=${"The Environment"}&body=${emailText}`}>
                            <FontAwesomeIcon icon={faEnvelope} onClick={() => handleContactClick()}/>
                        </a>
                    </footer>
                    </>
                    :
                    !token?
                    <h3>Sign in to contact this MP</h3>
                    :
                    <h3>You've contacted an MP too recently, please try again later</h3>
}
                </header>
            </section>

            <section className="mp-votes">
                <main>
                    <div>
                        <h2>Environmental Voting History</h2>
                        <table>
                            <thead className="mp-vote-table">
                                <tr className="mp-vote-titles">
                                    <th>Bill</th>
                                    <th>Vote</th>
                                </tr>
                            </thead>
                            {open ? 
                            <>
                                <VotesList mpVotes={mpVotes}/>
                                <button type='button' className="collapse-votes-btn"onClick={() => setOpen(!open)}>See less votes ⬆</button>
                            </>
                            :
                                mpVotes.length > 0 ?
                                    mpVotes.length >= 5 ?
                                        <>
                                            <VotesList mpVotes={mpVotes.slice(0, 5)}/>
                                            <button type='button' className="collapse-votes-btn"onClick={() => setOpen(!open)}>See more votes ⬇</button>
                                        </>
                                    :
                                        <VotesList mpVotes={mpVotes.slice(0, mpVotes.length)}/>
                                :
                                <></>

                            }
                        </table>
                        
                    </div>
                </main>
            </section>

        </section>
    )
}

export default MP;