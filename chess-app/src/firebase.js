// firebase.js
import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";

const firebaseConfig = {
    apiKey: "AIzaSyB5_VYhobiflSgq9Y7y4cFDbTUGN26GaSE",
    authDomain: "chess-7352a.firebaseapp.com",
    projectId: "chess-7352a",
    storageBucket: "chess-7352a.firebasestorage.app",
    messagingSenderId: "404402015855",
    appId: "1:404402015855:web:66102d8dd566188e62479a",
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

export { auth, provider, signInWithPopup };
