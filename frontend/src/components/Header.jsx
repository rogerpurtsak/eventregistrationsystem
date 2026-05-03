export default function Header({ adminToken, onLoginClick, onLogout, onCreateEvent }) {
  return (
    <header>
      <h1>Event Registration</h1>
      <div>
        {!adminToken ? (
          <button onClick={onLoginClick}>Admin Login</button>
        ) : (
          <>
            <button onClick={onCreateEvent}>Create Event</button>
            <button onClick={onLogout}>Logout</button>
          </>
        )}
      </div>
    </header>
  );
}
