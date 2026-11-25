import axios from 'axios';
import { useEffect, useState } from 'react';
import LibraryButton from './LibraryButton';

const API_URL = 'http://localhost:8082/api/offices';
const EMPTY_FORM = { name: '', officeNumber: '' };

function showError(error) {
    const data = error.response?.data;
    alert(typeof data === 'string' && data ? data : 'Cannot reach the server (is Spring Boot running on port 8082?)');
}

function Office() {
    const [form, setForm] = useState(EMPTY_FORM);   // save form
    const [searchText, setSearchText] = useState('');
    const [offices, setOffices] = useState([]);
    const [edit, setEdit] = useState(null);          // row being updated: { id, name, officeNumber }

    // load all offices, or only the ones matching the search text
    async function loadOffices(text = searchText) {
        try {
            const url = text.trim() ? `${API_URL}/find/name` : `${API_URL}/all`;
            const response = await axios.get(url, { params: { name: text.trim() } });
            setOffices(response.data);
        } catch (error) {
            showError(error);
        }
    }

    // runs a save/update/delete request, shows the server message, then runs `after`
    async function run(request, after) {
        try {
            alert((await request).data);
            after();
        } catch (error) {
            showError(error);
        }
    }

    useEffect(() => {
        loadOffices('');
    }, []);

    const saveOffice = () =>
        run(axios.post(`${API_URL}/save`, { ...form, officeNumber: Number(form.officeNumber) }), () => {
            setForm(EMPTY_FORM);
            setSearchText('');
            loadOffices('');
        });

    const deleteOffice = (id) =>
        window.confirm('Are you sure you want to delete this office?') &&
        run(axios.delete(`${API_URL}/delete/${id}`), loadOffices);

    const updateOffice = () =>
        run(axios.put(`${API_URL}/update/${edit.id}`, { name: edit.name, officeNumber: Number(edit.officeNumber) }), () => {
            setEdit(null);
            loadOffices();
        });

    const showAll = () => {
        setSearchText('');
        loadOffices('');
    };

    const changeForm = (e) => setForm({ ...form, [e.target.name]: e.target.value });
    const changeEdit = (e) => setEdit({ ...edit, [e.target.name]: e.target.value });

    return (
        <div>
            <h1>Save OFFICE</h1>
            <label>Office Name:</label>
            <input type='text' name='name' value={form.name} onChange={changeForm} /><br />
            <label>Office Number:</label>
            <input type='number' name='officeNumber' value={form.officeNumber} onChange={changeForm} /><br />
            <LibraryButton buttonLabel="SAVE" onClickButton={saveOffice} />

            <h2>Offices</h2>
            <input
                type='text'
                value={searchText}
                placeholder="Search office by name"
                onChange={(e) => setSearchText(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && loadOffices()}
            />
            <LibraryButton buttonLabel="SEARCH" onClickButton={() => loadOffices()} />
            <LibraryButton buttonLabel="SHOW ALL" onClickButton={showAll} />

            <table border="1" cellPadding="8" style={{ borderCollapse: 'collapse', marginTop: '12px', width: '100%' }}>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Office Name</th>
                        <th>Office Number</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {offices.length === 0 && (
                        <tr><td colSpan="4" style={{ textAlign: 'center' }}>No offices found</td></tr>
                    )}
                    {offices.map((office, index) => {
                        const editing = edit?.id === office.id;
                        return (
                            <tr key={office.id}>
                                <td>{index + 1}</td>
                                <td>{editing ? <input type='text' name='name' value={edit.name} onChange={changeEdit} /> : office.name}</td>
                                <td>{editing ? <input type='number' name='officeNumber' value={edit.officeNumber} onChange={changeEdit} /> : office.officeNumber}</td>
                                <td>
                                    {editing ? (
                                        <>
                                            <LibraryButton buttonLabel="SAVE CHANGES" onClickButton={updateOffice} />
                                            <LibraryButton buttonLabel="CANCEL" onClickButton={() => setEdit(null)} />
                                        </>
                                    ) : (
                                        <>
                                            <LibraryButton buttonLabel="DELETE" onClickButton={() => deleteOffice(office.id)} />
                                            <LibraryButton buttonLabel="UPDATE" onClickButton={() => setEdit({ ...office })} />
                                        </>
                                    )}
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );
}

export default Office;