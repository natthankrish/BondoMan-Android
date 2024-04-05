const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql');

const app = express();
const port = 3000;

// Create a MySQL connection
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  database: 'bondoman'
});

// Connect to MySQL
connection.connect((err) => {
  if (err) {
    console.error('Error connecting to MySQL: ' + err.stack);
    return;
  }
  console.log('Connected to MySQL as id ' + connection.threadId);
});

// Middleware to parse JSON bodies
app.use(bodyParser.json());

// Login endpoint
app.post('/api/auth/login', (req, res) => {
  const email = req.body.email;
  const password = req.body.password;

  console.log(`Password: ${password}`)

  // Construct SQL query (unsafe, vulnerable to SQL injection)
  const sql = `SELECT * FROM users WHERE email='${email}' AND password='${password}'`;

  // Execute the query
  connection.query(sql, (err, results) => {
    if (err) {
      console.error('Error executing SQL query: ' + err);
      res.status(500).json({ error: 'Internal Server Error' });
      return;
    }

    // Check if user was found
    if (results.length > 0) {
      res.status(200).json({ token: 'abcd' });
    } else {
      res.status(401).json({ message: 'Login failed' });
    }
  });
});

app.post('/api/auth/token', (req, res) => {
    res.status(200).json({
        nim: "13521170",
        iat: "1712071434",
        exp: "1712071734"
    })
})

// Start the server
app.listen(port, () => {
  console.log(`Server listening at http://localhost:${port}`);
});
