
const express = require('express')
const axios = require('axios');

const app = express()
var bodyParser = require('body-parser')
var multer = require('multer');
var upload = multer();


app.use(bodyParser.urlencoded({ extended: true })); 
app.use(upload.array()); 


app.use(express.static(__dirname + "/public"))
app.use('/css', express.static(__dirname + 'public/css'))
app.use('/js', express.static(__dirname + 'public/js'))
app.use('/imgs', express.static(__dirname + 'public/imgs'))
const port = 8000 

app.set('views', './views')
app.set('view engine', 'ejs')


app.get('', (req, res) => {
    res.render('index')
})

app.get('/signup', (req, res) => {
    res.render('signup')
})

app.get('/test', (req, res) => {
    res.render('test')
})

app.post('/register', (req,res)=>{
    
    var name = req.body.name;
    var email =req.body.email;
    var pass = req.body.password;
    
  
    var data = {
        "name": name,
        "email":email,
        "password":pass
    }
    console.info(data)

    axios.post('http://localhost:8080/signup', data)
    .then(response =>{
        console.log(response.status)
        if (response.status==200){
            return res.redirect('/');
        }
        } )
    .catch(error => {
        console.error('There was an error!', error);
    });
     
})

app.post('/signin', (req,res)=>{
    var email =req.body.email;
    var pass = req.body.password;
    
  
    var data = {
        "email":email,
        "password":pass
    }
    console.info(data)

    axios.post('http://localhost:8080/signin', data)
    .then(response =>{
        console.log(response.status)
        if (response.status==200){
            return res.render('search');
        }
        } )
    .catch(error => {
        console.error('There was an error!', error);
    });
    // return res.redirect('signup_success.html');
})

app.listen(port,  () => console.info("listening on ${port}"))