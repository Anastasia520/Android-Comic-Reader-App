const express = require("express");
const app = express();
require("dotenv").config();
const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`App listening at http://localhost:${PORT}`);
});

app.use(express.static(__dirname + "/img/"));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use("/manga", require("./routes/manga.routes"));

app.use("/check", (req, res) => {
  res.send('all good');
});