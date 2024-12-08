const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
const port = 3000;
const serverName = 'sensor-demo-test-api';

// 解析 JSON 请求体
app.use(express.json());

app.get('/', (req, res) => {
  res.json({ test: "this is base url" });
});

// 新的 API 接口
app.post('/post-json-data', (req, res) => {
  const postData = req.body;

  // 直接在终端显示 json 字串
  console.log('Received json:', postData);

  // 返回成功响应
  res.sendStatus(200);
});

app.listen(port, () => {
  console.log(`${serverName} is running on http://localhost:${port}`);
});