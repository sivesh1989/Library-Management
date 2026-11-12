#!/bin/bash
# Quick smoke test with curl (run from Git Bash in this folder). Services on 8081 and 8082.
M=http://localhost:8081
B=http://localhost:8082
H='Content-Type: application/json'
echo "--- create members"; curl -s -X POST $M/api/members -H "$H" -d @member1.json; echo
curl -s -X POST $M/api/members -H "$H" -d @member2.json; echo
echo "--- all members";    curl -s $M/api/members; echo
echo "--- add books";      curl -s -X POST $B/api/books -H "$H" -d @book1.json; echo
curl -s -X POST $B/api/books -H "$H" -d @book2.json; echo
echo "--- borrow 1 -> 1";  curl -s -X POST $B/api/loans -H "$H" -d @borrow1.json; echo
echo "--- same again (409)"; curl -s -X POST $B/api/loans -H "$H" -d @borrow1.json; echo
echo "--- no copies (409)";  curl -s -X POST $B/api/loans -H "$H" -d @borrow2.json; echo
echo "--- all loans";      curl -s $B/api/loans; echo
echo "--- return loan 1";  curl -s -X PUT $B/api/loans/1/return; echo
echo "--- borrow 2 -> 1";  curl -s -X POST $B/api/loans -H "$H" -d @borrow2.json; echo
echo "--- loans of member 2"; curl -s $B/api/loans/member/2; echo
