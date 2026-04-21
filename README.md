Для применения всех манифестов

```bash
 kubectl apply -f k8s/
 ```
Посмотреть все поды
```bash
  kubectl get pods
 ```
Чтобы отправить запрос к AuthService в Postman, необходимо прописать в терминале следующую команду

```bash
 kubectl port-forward pod/authservice-847898bcf4-2j5ds 8080:8080
 ```