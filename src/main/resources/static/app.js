const API = "/api/students";
let students = [];
const $ = (id) => document.getElementById(id);

async function request(url, options = {}) {
  const response = await fetch(url, options);
  if (!response.ok) {
    let message = "Something went wrong";
    try {
      const data = await response.json();
      message = data.message || data.error || message;
    } catch {
      // The response may not contain JSON.
    }
    throw new Error(message);
  }
  return response.status === 204 ? null : response.json();
}

function toast(message) {
  $("toast").textContent = message;
  $("toast").classList.add("show");
  clearTimeout(toast.t);
  toast.t = setTimeout(() => $("toast").classList.remove("show"), 2300);
}

async function load() {
  try {
    students = await request(API);
    render();
    const stats = await request(`${API}/stats`);
    $("total").textContent = stats.total;
    $("courses").textContent = stats.courses;
    $("average").textContent = `${stats.averageMarks}%`;
    $("low").textContent = stats.lowAttendance;
  } catch (error) {
    $("student-rows").innerHTML =
      `<tr><td colspan="7" class="empty">${error.message}. Is the Spring Boot server running?</td></tr>`;
  }
}

function initials(name) {
  return name
    .split(" ")
    .map((value) => value[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();
}

function render() {
  const query = $("search").value.toLowerCase();
  const course = $("course-filter").value;
  const list = students.filter(
    (student) =>
      (!course || student.course === course) &&
      [student.name, student.email, student.course].some((value) =>
        value.toLowerCase().includes(query),
      ),
  );

  $("row-count").textContent = `${list.length} records`;
  $("student-rows").innerHTML = list.length
    ? list
        .map(
          (student) => `
            <tr>
                <td>
                    <div class="student">
                        <div class="student-avatar">${initials(student.name)}</div>
                        <div>
                            <b>${esc(student.name)}</b>
                            <small>${esc(student.email)}</small>
                        </div>
                    </div>
                </td>
                <td>${esc(student.course)}</td>
                <td>${esc(student.year)}</td>
                <td>
                    <span class="bar ${student.attendance < 75 ? "low" : ""}">
                        <i style="width:${student.attendance}%"></i>
                    </span>
                    ${student.attendance}%
                </td>
                <td><b>${Number(student.marks).toFixed(1)}%</b></td>
                <td>
                    <span class="pill ${student.attendance < 75 ? "warning" : "good"}">
                        ${student.attendance < 75 ? "Low attendance" : "On track"}
                    </span>
                </td>
                <td class="actions">
                    <button onclick="editStudent(${student.id})">Edit</button>
                    <button class="del" onclick="deleteStudent(${student.id})">Delete</button>
                </td>
            </tr>`,
        )
        .join("")
    : '<tr><td colspan="7" class="empty">No matching students found.</td></tr>';
}

function esc(value) {
  return String(value ?? "").replace(
    /[&<>"']/g,
    (character) =>
      ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;",
      })[character],
  );
}

function openModal(student) {
  $("student-form").reset();
  $("student-form").elements.id.value = student?.id || "";
  $("modal-title").textContent = student ? "Edit student" : "Add student";
  if (student) {
    Object.entries(student).forEach(([key, value]) => {
      if ($("student-form").elements[key]) {
        $("student-form").elements[key].value = value;
      }
    });
  }
  $("modal-bg").classList.add("show");
}

function closeModal() {
  $("modal-bg").classList.remove("show");
}

async function editStudent(id) {
  const student = students.find((value) => value.id === id);
  if (student) {
    openModal(student);
  }
}

async function deleteStudent(id) {
  const student = students.find((value) => value.id === id);
  if (!student || !confirm(`Delete ${student.name}'s record?`)) {
    return;
  }
  try {
    await request(`${API}/${id}`, { method: "DELETE" });
    toast("Student record deleted");
    await load();
  } catch (error) {
    toast(error.message);
  }
}

$("add-student").onclick = () => openModal();
$("close-modal").onclick = closeModal;
$("cancel").onclick = closeModal;
$("modal-bg").onclick = (event) => {
  if (event.target === $("modal-bg")) {
    closeModal();
  }
};
$("search").oninput = render;
$("course-filter").onchange = render;

$("student-form").onsubmit = async (event) => {
  event.preventDefault();
  const form = event.currentTarget;
  const formData = new FormData(form);
  const id = formData.get("id");
  const data = Object.fromEntries(formData.entries());
  delete data.id;
  data.attendance = Number(data.attendance);
  data.marks = Number(data.marks);
  const button = $("save-btn");
  button.disabled = true;
  button.textContent = "Saving…";
  try {
    await request(id ? `${API}/${id}` : API, {
      method: id ? "PUT" : "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    closeModal();
    toast(id ? "Student updated" : "Student added successfully");
    await load();
  } catch (error) {
    toast(error.message);
  } finally {
    button.disabled = false;
    button.textContent = "Save student";
  }
};

$("export").onclick = () => {
  const rows = [
    ["Name", "Email", "Phone", "Course", "Year", "Attendance", "Marks"],
    ...students.map((student) => [
      student.name,
      student.email,
      student.phone,
      student.course,
      student.year,
      student.attendance,
      student.marks,
    ]),
  ];
  const csv = rows
    .map((row) =>
      row
        .map((value) => `"${String(value ?? "").replace(/"/g, '""')}"`)
        .join(","),
    )
    .join("\n");
  const link = document.createElement("a");
  link.href = URL.createObjectURL(new Blob([csv], { type: "text/csv" }));
  link.download = "campustrack-students.csv";
  link.click();
  URL.revokeObjectURL(link.href);
};

load();
