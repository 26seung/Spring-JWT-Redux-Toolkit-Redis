import React, { useEffect, useState } from "react";
import api from "services/api";

const GuestPage = () => {
  const [data, setData] = useState("");

  useEffect(() => {
    api
      .get("/user/guest")
      .then((res) => {
        // console.log("GuestPage : ", res);
        setData(res.data);
        return res;
      })
      .catch((err) => {
        console.log("Guest Page error ", err);
        setData(err.message);
        return err;
      });
  }, []);
  return <div>{data}</div>;
};

export default GuestPage;
