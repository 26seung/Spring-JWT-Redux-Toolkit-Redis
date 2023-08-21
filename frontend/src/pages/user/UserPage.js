import React, { useEffect, useState } from "react";
import api from "services/api";

const UserPage = () => {
  const [data, setData] = useState("");

  useEffect(() => {
    api
      .get("/user/user")
      .then((res) => {
        // console.log(res);
        setData(res.data);
        return res;
      })
      .catch((err) => {
        console.log("User Page error ", err);
        setData(err.message);
        return err;
      });
  }, []);
  return <div>{data}</div>;
};

export default UserPage;
