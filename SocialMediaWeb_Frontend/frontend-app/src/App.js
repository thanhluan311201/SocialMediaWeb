
import './App.css';
import Login from './components/User/Login';

function App() {
  return (
    <div>
      <div className='greeting'>
        <h1>SocialMediaWeb</h1>
        <h2>The name speaks for itself. Try it out.</h2>
      </div>
      <div className='login'>
        <Login />
      </div>
    </div>
    
  );
}

export default App;
