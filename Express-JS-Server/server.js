const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
const port = 1234;
const serverName = 'sensor-demo-test-api';

// 解析 JSON 请求体
app.use(express.json());

app.get('/', (res) => {
  res.json({ test: "this is base url" });
});

// 新的 API 接口
app.post('/api/save', (req, res) => {
  const postData = req.body;

  // 直接在终端显示 json 字串
  console.log('Received json:', JSON.stringify(postData, null, 2));

  // 返回成功响应
  res.status(200).json(200);
});

app.listen(port, () => {
  console.log(`${serverName} is running on http://localhost:${port}`);
});