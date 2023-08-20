import React, { useEffect, useState } from "react";
import api from "services/api";

const AdminPage = () => {
  const [data, setData] = useState("");

  useEffect(() => {
    api
      .get("/user/admin")
      .then((res) => {
        // console.log(res);
        setData(res.data);
        return res;
      })
      .catch((err) => {
        // console.log(err);
        setData(err.message);
        return err;
      });
  }, []);
  return <div>{data}</div>;
};

export default AdminPage;
