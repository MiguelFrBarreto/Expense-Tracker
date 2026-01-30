
async function isLogged() {
    const response = await fetch("http://localhost:8080/api/auth/@me")
    if (!response.ok) {
        window.location.href = "/login.html"
    }

    const user = await response.json();

    document.getElementById("userName").textContent = "User: " + user.name;
}

isLogged()

async function updateExpenses() {
    const list = document.getElementById("expenses")

    const response = await fetch("http://localhost:8080/api/expenses")

    if (!response.ok) {
        alert("ERRO: não foi possível encontrar expenses")
    }

    const expenses = await response.json()

    expenses.forEach(e => {
        const li = document.createElement("li")
        const p = document.createElement("p")
        p.textContent = e.description + "|" + e.amount + "|" + e.date + "|" + e.type + "|" + e.category.name
        li.appendChild("p")
        li.dataset.id = e.id

        const deleteButton = document.createElement("button")
        deleteButton.type = "button"
        deleteButton.textContent = "Delete"

        deleteButton.addEventListener("click", async function () {
            const url = `http://localhost:8080/api/expenses/${e.id}`
            const deleteResponse = await fetch(url, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            })
            if (!deleteResponse.ok) {
                alert("Erro ao deletar expense")
                alert(deleteResponse.status)
            }
            li.remove()
        })

        li.appendChild(deleteButton)
        list.appendChild(li)
    });
}

async function updateCategories() {
    const list = document.getElementById("categories")

    const response = await fetch("http://localhost:8080/api/categories")

    if (!response.ok) {
        alert("ERRO: não foi possível encontrar categories")
    }

    const categories = await response.json()

    categories.forEach(c => {
        const li = document.createElement("li")
        const p = document.createElement("p")
        p.textContent = c.name
        li.appendChild(p)
        li.dataset.id = c.id

        if (c.userId !== null) {
            const deleteButton = document.createElement("button")
            deleteButton.type = "button"
            deleteButton.textContent = "Delete"

            deleteButton.addEventListener("click", async function () {
                const url = `http://localhost:8080/api/categories/${c.id}`
                const deleteResponse = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include'
                })
                if (!deleteResponse.ok) {
                    alert("Erro ao deletar category")
                    alert(deleteResponse.status)
                }
                li.remove()
            })

            li.appendChild(deleteButton)
        }

        list.appendChild(li)

        const select = document.getElementById("selectCategory")
        const option = document.createElement("option")
        option.textContent = li.querySelector("p").textContent
        option.value = li.dataset.id
        select.appendChild(option)
    });
}

updateCategories()
updateExpenses()

const createExpense = document.getElementById("createExpense")
const createCategory = document.getElementById("createCategory")

createExpense.addEventListener("click", async function () {
    const list = document.getElementById("expenses")
    const description = document.getElementById("expenseDescription").value
    const amount = document.getElementById("expenseAmount").value
    const date = document.getElementById("expenseDate").value
    const type = document.querySelector('input[name="type"]:checked')?.value;
    const category = document.getElementById("selectCategory").value

    const response = await fetch("http://localhost:8080/api/expenses", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
            description: description,
            amount: amount,
            date: date + "T00:00:00Z",
            type: type,
            categoryId: category
        })
    })
    if (response.ok) {
        const e = await response.json()
        const li = document.createElement("li")
        p = document.createElement("p")
        p.textContent = e.description + "|" + e.amount + "|" + e.date + "|" + e.type + "|" + e.category.name
        li.appendChild(p)
        li.dataset.id = e.id

        const deleteButton = document.createElement("button")
        deleteButton.type = "button"
        deleteButton.textContent = "Delete"
        deleteButton.addEventListener("click", async function () {
            const url = `http://localhost:8080/api/expenses/${e.id}`
            const deleteResponse = await fetch(url, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'

            })
            if (!deleteResponse.ok) {
                alert("Erro ao deletar expense")
                alert(deleteResponse.status)
            }
            li.remove()
        })

        li.appendChild(deleteButton)
        list.appendChild(li)
    } else {
        alert("Erro ao criar expense")
    }
})

createCategory.addEventListener("click", async function () {
    const list = document.getElementById("categories")
    const name = document.getElementById("categoryName").value

    const response = await fetch("http://localhost:8080/api/categories", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ name: name })
    })
    if (response.ok) {
        const c = await response.json()
        const li = document.createElement("li")
        const p = document.createElement("p")
        p.textContent = name
        li.appendChild(p)

        const deleteButton = document.createElement("button")
        deleteButton.type = "button"
        deleteButton.textContent = "Delete"
        deleteButton.addEventListener("click", async function () {
            const url = `http://localhost:8080/api/categories/${c.id}`
            const deleteResponse = await fetch(url, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            })
            if (!deleteResponse.ok) {
                alert("Erro ao deletar category")
                alert(deleteResponse.status)
            }
            li.remove()
        })

        li.appendChild(deleteButton)
        list.appendChild(li)
    } else {
        alert("Erro ao criar category")
    }
})