import React, { useState } from 'react'
import LibraryButton from './LibraryButton'
function LibraryLogin() {
   const [doYouHaveAnAccount, setDoYouHaveAnAccount] = useState(true);
   const [UserName, setUserName] = useState("");
   const [Password, setPassword] = useState("");
   const [StudentId, setStudentId] = useState("");
   const [Email, setEmail] = useState("");
   const [ConfirmPassword, setConfirmPassword] = useState("");

   function HandleNavigate(){
    setDoYouHaveAnAccount(!doYouHaveAnAccount);
   }
   function HandleLogin(){
    console.log("Login data:", { UserName, Password });
    alert(`Welcome ${UserName}! You have successfully logged in.`);
   }

   function HandleSignUp(){
    if (Password !== ConfirmPassword) {
      alert("Passwords do not match");
      return;
    }
    console.log("Sign-up data:", { StudentId, Email, Password });
   }
  return (
    <>
    { doYouHaveAnAccount ? (
                    <div style={{border:"3px solid blue", marginTop:"190px"}}>
                        <label>UserName:</label>
                        <input type="text" value={UserName} onChange={e=> setUserName(e.target.value)} placeholder='Enter Your username'/><br/>
                        <label>Password:</label>
                        <input type="password" value={Password} onChange={e=> setPassword(e.target.value)} /><br/>
                        <LibraryButton buttonLabel="LOGIN" onClickButton={HandleLogin}/> <br/>
                        <label>New here? </label> <LibraryButton buttonLabel="Create Account" onClick={HandleNavigate}/>
                    </div>
    ):(
            <div style={{border:"3px solid indigo", marginTop:"190px"}}>
                <label>Student ID:</label>
                <input type="text" value={StudentId} onChange={e=> setStudentId(e.target.value)} placeholder='Enter Your Student ID'/><br/>
                <label>Email:</label>
                <input type="email" value={Email} onChange={e=> setEmail(e.target.value)} /><br/>
                <label>Password:</label>
                <input type="password" value={Password} onChange={e=> setPassword(e.target.value)} /><br/>
                <label>Confirm Password:</label>
                <input type="password" value={ConfirmPassword} onChange={e=> setConfirmPassword(e.target.value)} /><br/>
                <LibraryButton buttonLabel="SIGN UP" onClick={HandleSignUp}/> <br/>
                 <label>Already have an account?? </label> <LibraryButton buttonLabel="Sign In" onClick={HandleNavigate}/>
            </div>
    )
     }
    </>
  )
}

export default LibraryLogin