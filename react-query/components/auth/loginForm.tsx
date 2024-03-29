"use client";

import { cn } from "@/lib/utils";
import React from "react";
import { Label } from "../ui/label";
import { Input } from "../ui/input";
import { Button, buttonVariants } from "../ui/button";
import Link from "next/link";
import Image from "next/image";

const LoginForm = () => {
  const [isLoading, setIsLoading] = React.useState<boolean>(false);
  async function onSubmit(event: React.SyntheticEvent) {
    event.preventDefault();
    setIsLoading(true);

    setTimeout(() => {
      setIsLoading(false);
    }, 3000);
  }

  return (
    <>
      <div className="w-full lg:grid min-h-screen lg:grid-cols-2">
        <div className="flex items-center justify-center py-12">
          <div className="mx-auto grid w-[350px] gap-6">
            <div className="grid gap-2 text-center">
              <h1 className="text-3xl font-bold">로그인</h1>
              <p className="text-balance text-muted-foreground">
                ID / PW 로그인
              </p>
            </div>
            <div className="grid gap-4">
              <div className="grid gap-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="m@example.com"
                  required
                />
              </div>
              <div className="grid gap-2">
                <div className="flex items-center">
                  <Label htmlFor="password">Password</Label>
                </div>
                <Input id="password" type="password" required />
              </div>
              <Button type="submit" className="w-full mt-5">
                Login
              </Button>
            </div>

            <div className="mt-4 text-center text-sm">
              <div>
                <Link
                  href=""
                  className="ml-auto inline-block text-sm underline mb-2"
                >
                  Forgot your password?
                </Link>
              </div>
              <div>
                Don&apos;t have an account?
                <Link href="/auth/signup" className="underline ml-1">
                  Sign up
                </Link>
              </div>
            </div>
          </div>
        </div>
        <div className="hidden bg-muted lg:block">
          <Image
            src="/vercel.svg"
            alt="Image"
            width="1920"
            height="1080"
            className="h-full w-full object-cover dark:brightness-[0.2] dark:grayscale translate-x-10"
          />
        </div>
      </div>
    </>
  );
};

export default LoginForm;
