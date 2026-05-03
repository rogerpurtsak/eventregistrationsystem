import { useState } from 'react';

export default function RegistrationForm({ event, onSubmit, onCancel }) {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [personalCode, setPersonalCode] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  function validate() {
    if (!firstName.trim()) return 'First name is required.';
    if (!lastName.trim()) return 'Last name is required.';
    if (!personalCode.trim()) return 'Personal code is required.';
    if (personalCode.trim().length !== 11) return 'Personal code must be 11 characters.';
    return null;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }
    setLoading(true);
    try {
      await onSubmit(event.id, { firstName, lastName, personalCode });
      setSuccess(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  if (success) {
    return (
      <div className="event-card">
        <p className="success">Registration successful!</p>
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
        placeholder="Personal code (11 characters)"
        value={personalCode}
        onChange={e => setPersonalCode(e.target.value)}
      />
      {error && <p className="error">{error}</p>}
      <div style={{ display: 'flex', gap: '0.5rem' }}>
        <button type="submit" disabled={loading}>
          {loading ? 'Registering...' : 'Register'}
        </button>
        <button type="button" onClick={onCancel}>Cancel</button>
      </div>
    </form>
  );
}
