import React, { useState } from 'react';
import './Login.css';
import { useNavigate } from "react-router-dom";
import { FaUser, FaLock, FaCalendarAlt } from "react-icons/fa";
import { logIn, register } from '../../services/authenticationService';


export default function Login(){
    const navigate = useNavigate();

    const [isRegister, setIsRegister] = useState(false);

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [dob, setDob] = useState("");

    const handleSubmitLogin = async (event) => {
        event.preventDefault();
    
        try {
          const response = await logIn(username, password);
          navigate("/home");
          console.log("Response body:", response.data);
        } catch (error) {
          const errorResponse = error.response.data;
          alert(errorResponse.message);
        }
    };

    const handleSubmitRegister = async (event) => {
        event.preventDefault();

        try {
            const response = await register(username, password, firstname, lastname, dob);
            console.log("Response body:", response.data);
            navigate("/");
            alert("Account registration successful.");
        } catch (error) {
            const errorResponse = error.response.data;
            alert(errorResponse.message);
        }
    };
    return (
        <div>
            <div className='greeting'>
                <h1>SocialMediaWeb</h1>
                <h2>The name speaks for itself. Try it out.</h2>
            </div>
            <div className={`wrapper${isRegister ? ' active' : ''}`}>
                <div className='form-box login'>
                    <form action=''>
                        <h1>Login</h1>
                        <div className='input-box'>
                            <input type='text' placeholder='Username' value={username} 
                                                                        onChange={(e) => setUsername(e.target.value)} required/>
                            <FaUser className='icon'/>
                        </div>
                        <div className='input-box'>
                            <input type='password' placeholder='Password' value={password} 
                                                                            onChange={(e) => setPassword(e.target.value)} required/>
                            <FaLock className='icon'/>
                        </div>
                        <div className='remember-forgot'>
                            <a href='/#'>Forgot password?</a>
                        </div>

                        <button type='submit' onClick={handleSubmitLogin}>Login</button>

                        <div className='register-link'>
                            <p>Don't have account? <a onClick={() => setIsRegister(true)}>Register</a></p>
                        </div>
                    </form>
                </div>
                <div className='form-box register'>
                    <form action=''>
                        <h1>Registration</h1>
                        <div className='input-box'>
                            <input type='text' placeholder='Username' value={username} 
                                                                        onChange={(e) => setUsername(e.target.value)} required/>
                            <FaUser className='icon'/>
                        </div>
                        <div className='input-box'>
                            <input type='text' placeholder='First name' value={firstname} 
                                                                        onChange={(e) => setFirstname(e.target.value)} required/>
                            <FaUser className='icon'/>
                        </div>
                        <div className='input-box'>
                            <input type='text' placeholder='Last name' value={lastname} 
                                                                        onChange={(e) => setLastname(e.target.value)} required/>
                            <FaUser className='icon'/>
                        </div>
                        <div className='input-box'>
                            <input type='date' placeholder='Date of birth' value={dob} 
                                                                        onChange={(e) => setDob(e.target.value)} required/>
                            <FaCalendarAlt  className='icon'/>
                        </div>
                        <div className='input-box'>
                            <input type='password' placeholder='Password' value={password} 
                                                                            onChange={(e) => setPassword(e.target.value)} required/>
                            <FaLock className='icon'/>
                        </div>
                        <div className='remember-forgot'>
                            <label><input type='checkbox' required/>I agree the terms & conditions</label>
                        </div>

                        <button type='submit' onClick={handleSubmitRegister}>Register</button>

                        <div className='register-link'>
                            <p>Already have an account? <a onClick={() => setIsRegister(false)}>Login</a></p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}
