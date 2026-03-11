import React, { useState, useEffect, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { chatService } from '../services/api';

const ChatPage = () => {
    const { user } = useAuth();
    const [threads, setThreads] = useState([]);
    const [selectedThread, setSelectedThread] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [loadingThreads, setLoadingThreads] = useState(true);
    const [loadingMessages, setLoadingMessages] = useState(false);

    const messagesEndRef = useRef(null);

    useEffect(() => {
        const fetchThreads = async () => {
            try {
                const response = await chatService.getThreads(user.id);
                if (response.data.status === 'success') {
                    setThreads(response.data.threads || []);
                }
            } catch (err) {
                console.error('Failed to fetch threads', err);
            } finally {
                setLoadingThreads(false);
            }
        };

        fetchThreads();
    }, [user.id]);

    useEffect(() => {
        if (selectedThread) {
            const fetchMessages = async () => {
                setLoadingMessages(true);
                try {
                    const response = await chatService.getMessages(user.id, selectedThread.id);
                    if (response.data.status === 'success') {
                        setMessages(response.data.messages || []);
                    }
                } catch (err) {
                    console.error('Failed to fetch messages', err);
                } finally {
                    setLoadingMessages(false);
                }
            };

            fetchMessages();
            const interval = setInterval(fetchMessages, 5000); // Polling every 5 seconds
            return () => clearInterval(interval);
        }
    }, [selectedThread, user.id]);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!newMessage.trim() || !selectedThread) return;

        const messageData = {
            sender_id: user.id,
            receiver_id: selectedThread.id,
            message_text: newMessage
        };

        try {
            const response = await chatService.sendMessage(messageData);
            if (response.data.status === 'success') {
                setNewMessage('');
                // Refresh messages
                const res = await chatService.getMessages(user.id, selectedThread.id);
                setMessages(res.data.messages || []);
            }
        } catch (err) {
            console.error('Failed to send message', err);
        }
    };

    return (
        <div className="page-content" style={{ height: 'calc(100vh - 180px)' }}>
            <div className="card" style={{ display: 'flex', height: '100%', padding: 0, overflow: 'hidden' }}>
                {/* Threads Sidebar */}
                <div style={{ width: '300px', borderRight: '1px solid #dfe1e6', overflowY: 'auto' }}>
                    <div style={{ padding: '1rem', borderBottom: '1px solid #dfe1e6' }}>
                        <h3>Messages</h3>
                    </div>
                    {loadingThreads ? (
                        <div style={{ padding: '1rem' }}>Loading chats...</div>
                    ) : threads.length > 0 ? (
                        threads.map(thread => (
                            <div
                                key={thread.id}
                                onClick={() => setSelectedThread(thread)}
                                style={{
                                    padding: '1rem',
                                    cursor: 'pointer',
                                    backgroundColor: selectedThread?.id === thread.id ? 'var(--bg-light)' : 'transparent',
                                    borderBottom: '1px solid #f4f5f7'
                                }}
                            >
                                <div style={{ fontWeight: 'bold' }}>{thread.senderName}</div>
                                <div style={{ fontSize: '0.8rem', color: 'var(--text-sub)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                                    {thread.lastMessage}
                                </div>
                            </div>
                        ))
                    ) : (
                        <div style={{ padding: '1rem', textAlign: 'center', color: 'var(--text-sub)' }}>No active chats</div>
                    )}
                </div>

                {/* Chat Area */}
                <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
                    {selectedThread ? (
                        <>
                            <div style={{ padding: '1rem', borderBottom: '1px solid #dfe1e6' }}>
                                <h4>{selectedThread.senderName}</h4>
                            </div>
                            <div style={{ flex: 1, padding: '1rem', overflowY: 'auto', backgroundColor: '#fafbfc' }}>
                                {loadingMessages && messages.length === 0 ? (
                                    <div>Loading messages...</div>
                                ) : (
                                    messages.map((msg, idx) => (
                                        <div
                                            key={msg.id || idx}
                                            style={{
                                                display: 'flex',
                                                justifyContent: msg.sender_id === user.id ? 'flex-end' : 'flex-start',
                                                marginBottom: '1rem'
                                            }}
                                        >
                                            <div
                                                style={{
                                                    maxWidth: '70%',
                                                    padding: '0.75rem 1rem',
                                                    borderRadius: '12px',
                                                    backgroundColor: msg.sender_id === user.id ? 'var(--primary-color)' : 'white',
                                                    color: msg.sender_id === user.id ? 'white' : 'var(--text-main)',
                                                    boxShadow: '0 1px 2px rgba(0,0,0,0.1)'
                                                }}
                                            >
                                                {msg.message_text}
                                                <div style={{ fontSize: '0.6rem', marginTop: '0.25rem', opacity: 0.7, textAlign: 'right' }}>
                                                    {new Date(msg.sent_at).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                                </div>
                                            </div>
                                        </div>
                                    ))
                                )}
                                <div ref={messagesEndRef} />
                            </div>
                            <form onSubmit={handleSendMessage} style={{ padding: '1rem', borderTop: '1px solid #dfe1e6', display: 'flex', gap: '1rem' }}>
                                <input
                                    type="text"
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    placeholder="Type a message..."
                                    style={{ flex: 1, padding: '0.75rem', borderRadius: '8px', border: '1px solid #dfe1e6' }}
                                />
                                <button type="submit" className="btn btn-primary">Send</button>
                            </form>
                        </>
                    ) : (
                        <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--text-sub)' }}>
                            Select a conversation to start chatting
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ChatPage;
