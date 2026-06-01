const API_URL = "http://localhost:8080/tasks";

// Load tasks when page opens
window.onload = function () {
    loadTasks();
};

// Load all tasks
function loadTasks() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const taskList = document.getElementById("taskList");
            taskList.innerHTML = "";

            data.forEach(task => {
                const li = document.createElement("li");

                li.innerHTML = 
                    task.title + " - " + task.description +
                    " <button onclick='deleteTask(" + task.id + ")'>Delete</button>";

                taskList.appendChild(li);
            });
        })
        .catch(error => {
            console.log("Error loading tasks:", error);
        });
}

// Add new task
function addTask() {
    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;

    if (title.trim() === "" || description.trim() === "") {
        alert("Enter title and description");
        return;
    }

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            title: title,
            description: description,
            completed: false
        })
    })
    .then(() => {
        loadTasks();
    });
}

// DELETE FUNCTION
function deleteTask(id) {
    fetch("http://localhost:8087/tasks/" + id, {
        method: "DELETE"
    })
    .then(() => {
        loadTasks();
    });
}