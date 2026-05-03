import { useState, useEffect } from 'react';
import Header from './components/Header';
import EventList from './components/EventList';
import LoginForm from './components/LoginForm';
import EventForm from './components/EventForm';
import RegistrationForm from './components/RegistrationForm';
import { getEvents, login, createEvent, registerToEvent } from './api/api';

export default function App() {
  const [events, setEvents] = useState([]);
  const [token, setToken] = useState(null);
  const [selectedEvent, setSelectedEvent] = useState(null);

  useEffect(() => {
    loadEvents();
  }, []);

  async function loadEvents() {
    try {
      const data = await getEvents();
      setEvents(data);
    } catch (err) {
      console.error(err);
    }
  }

  async function handleLogin(email, password) {
    const data = await login(email, password);
    setToken(data.token);
  }

  async function handleCreateEvent(eventData) {
    await createEvent(token, eventData);
    await loadEvents();
  }

  async function handleRegister(eventId, registrationData) {
    await registerToEvent(eventId, registrationData);
    await loadEvents();
  }

  return (
    <>
      <Header />
      <EventList events={events} onRegister={setSelectedEvent} />
      {selectedEvent && (
        <RegistrationForm
          event={selectedEvent}
          onSubmit={handleRegister}
          onCancel={() => setSelectedEvent(null)}
        />
      )}
      {!token ? (
        <LoginForm onLogin={handleLogin} />
      ) : (
        <EventForm onSubmit={handleCreateEvent} />
      )}
    </>
  );
}
