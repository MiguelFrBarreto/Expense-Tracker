async function isLogged(){
    const response = await fetch("http://localhost:8080/api/auth/@me")
    if(response.ok){
        window.location.href = "/index.html"
    }
}

isLogged()

const button = document.getElementById("register")

button.addEventListener("click", async function(){
    const name = document.getElementById("name").value
    const email = document.getElementById("email").value
    const password = document.getElementById("password").value

    console.log(email)
    console.log(password)

    const response = await fetch("http://localhost:8080/api/auth/register",{
        method:"POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({name: name,email: email, password: password},
        )
    })


    console.log(response.status)
    
    document.getElementById("name").value = ""
    document.getElementById("email").value = ""
    document.getElementById("password").value = ""
})