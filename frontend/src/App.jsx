import { useState, useEffect } from 'react';
import Header from './components/Header';
import EventList from './components/EventList';
import LoginForm from './components/LoginForm';
import EventForm from './components/EventForm';
import RegistrationForm from './components/RegistrationForm';
import { getEvents, loginAdmin, createEvent, registerToEvent } from './api/api';

export default function App() {
  const [events, setEvents] = useState([]);
  const [adminToken, setAdminToken] = useState(localStorage.getItem('adminToken'));
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [showLoginForm, setShowLoginForm] = useState(false);
  const [showEventForm, setShowEventForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    loadEvents();
  }, []);

  async function loadEvents() {
    setLoading(true);
    setError('');
    try {
      const data = await getEvents();
      setEvents(data);
    } catch (err) {
      setError('Failed to load events.');
    } finally {
      setLoading(false);
    }
  }

  async function handleLogin(email, password) {
    setError('');
    const data = await loginAdmin(email, password);
    localStorage.setItem('adminToken', data.token);
    setAdminToken(data.token);
    setShowLoginForm(false);
  }

  function handleLogout() {
    localStorage.removeItem('adminToken');
    setAdminToken(null);
    setShowEventForm(false);
  }

  async function handleCreateEvent(eventData) {
    setError('');
    await createEvent(eventData, adminToken);
    setSuccessMessage('Event created!');
    setTimeout(() => setSuccessMessage(''), 3000);
    setShowEventForm(false);
    await loadEvents();
  }

  async function handleRegister(eventId, registrationData) {
    setError('');
    await registerToEvent(eventId, registrationData);
    setSuccessMessage('Registration successful!');
    setTimeout(() => setSuccessMessage(''), 3000);
    setSelectedEvent(null);
    await loadEvents();
  }

  return (
    <>
      <Header
        adminToken={adminToken}
        onLoginClick={() => setShowLoginForm(true)}
        onLogout={handleLogout}
        onCreateEvent={() => setShowEventForm(true)}
      />

      {error && <p>{error}</p>}
      {successMessage && <p>{successMessage}</p>}

      {showLoginForm && (
        <LoginForm
          onLogin={handleLogin}
          onCancel={() => setShowLoginForm(false)}
        />
      )}

      {showEventForm && (
        <EventForm
          onSubmit={handleCreateEvent}
          onCancel={() => setShowEventForm(false)}
        />
      )}

      {selectedEvent && (
        <RegistrationForm
          event={selectedEvent}
          onSubmit={handleRegister}
          onCancel={() => setSelectedEvent(null)}
        />
      )}

      {loading ? (
        <p>Loading...</p>
      ) : (
        <EventList events={events} onRegister={setSelectedEvent} />
      )}
    </>
  );
}
