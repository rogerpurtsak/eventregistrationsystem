import { useState } from 'react';

export default function RegistrationForm({ event, onSubmit, onCancel }) {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [personalCode, setPersonalCode] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      await onSubmit(event.id, { firstName, lastName, personalCode });
      setSuccess(true);
    } catch (err) {
      setError(err.message);
    }
  }

  if (success) {
    return (
      <div>
        <p>Registration successful!</p>
        <button onClick={onCancel}>Close</button>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Register for: {event.title}</h2>
      <input
        type="text"
        placeholder="First name"
        value={firstName}
        onChange={e => setFirstName(e.target.value)}
      />
      <input
        type="text"
        placeholder="Last name"
        value={lastName}
        onChange={e => setLastName(e.target.value)}
      />
      <input
        type="text"
        placeholder="Personal code"
        value={personalCode}
        onChange={e => setPersonalCode(e.target.value)}
      />
      {error && <p>{error}</p>}
      <button type="submit">Register</button>
      <button type="button" onClick={onCancel}>Cancel</button>
    </form>
  );
}
