import React, { useEffect, useRef } from 'react'
import AuthHeader from '../../../components/axios/AuthHeader'

const SERVER_URL = process.env.REACT_APP_SERVER_URL;

export default function Clip(props) {
    const { url } = props
    const videoRef = useRef();
    const previousUrl = useRef(url);
  
    useEffect(() => {
      if (previousUrl.current === url) {
        return;
      }
  
      if (videoRef.current) {
        videoRef.current.load();
      }
  
      previousUrl.current = url;
    }, [url]);
  
    return (
      <video ref={videoRef} {...props}>
        <source src={`${SERVER_URL}/stream/video/${url}`} />
      </video>
    );
  }