import { faTimes, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { Component } from 'react'
import Dropzone from 'react-dropzone';
import { FormatBytes, TruncateSharp } from '../../../../components/helpers/FieldValidate';
import AdminService from '../../../../services/AdminService';
import VideoStream from '../../../public/Stream/VideoStream';
import './style.scss'

const SERVER_URL = process.env.REACT_APP_SERVER_URL;

export default class VideoModal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            files: [],
            video: ''
        };
    }

    upload = () => {
        const file = this.state.files[0]
        const form = new FormData()
        form.append('file', file)
        form.append('id', TruncateSharp(this.props.id))

        AdminService.uploadVideo(form)
        .then(response => {
            console.log(response)
        })
        .finally(() => this.refresh())
    }

    delete = () => {
        AdminService.deleteVideo(TruncateSharp(this.props.id))
        .finally(() => this.refresh())
    }

    onDrop = (files) => {
        this.setState({files})
    }

    refresh = () => {
        AdminService.findGiang(TruncateSharp(this.props.id))
        .then(response => {
            this.setState({
                video: response?.data?.video
            })
        })
        .finally(() => {
            this.setState({
                files: []
            })

            let video = document.getElementById('id')?.load()
            let source = video?.childNodes[0]
            video?.pause();

            source?.setAttribute('src', `${SERVER_URL}/stream/video/${this.state.video}`);

            video?.load();
            video?.play();
        })
    }

    componentDidMount() {
        this.refresh()
    }

    render() {
        let files = ''
        if (this.state.video) {
            files = <small className='text-secondary'>
                {this.state.video}
                {this.state.files.map(file => (
                    <span className='text-warning' key={file.name}> ==> {file.name}</span>
                ))}
            </small>
        } else {
            files = this.state.files.map(file => (
                <small className='text-secondary' key={file.name}>{file.name}</small>
            ));
        }
  
      return (
          <>
            <Dropzone onDrop={this.onDrop} multiple={false} accept={['video/mp4']}>
            {({getRootProps, getInputProps}) => (
                <section className="w-50 container border border-dark bg-white mt-4 shadow p-2 video">
                    <div className='d-flex'>
                        <div className='ms-auto btn btn-outline-danger' onClick={() => this.props.close()}>
                            <FontAwesomeIcon icon={faTimes} /> trở về
                        </div>
                    </div>
                    <hr />
                    <div {...getRootProps({ className: 'dropzone' })}>
                        <input {...getInputProps()} />
                        <p>{this.state.files[0] 
                            ? this.state.files[0].name + ' - ' + FormatBytes(this.state.files[0].size)
                            : 'Thả file hoặc click vào đây'}</p>
                    </div>
                    
                    <div className='d-flex'>
                        <div>
                            <h4>Video - {files}</h4>
                            </div>
                        
                        <div className='ms-auto'>
                            {(this.state.files[0] 
                                && <button className='btn btn-outline-warning' onClick={this.upload}>Cập nhật Video</button>)
                                || <button className='btn btn-danger' onClick={this.delete}>Xóa Video</button>}
                        </div>
                    </div>
                    <hr />
                    <div>
                        {this.state.video && <VideoStream id='video' url={this.state.video} controls loop className='w-100' />}
                    </div>
                </section>
            )}
            </Dropzone>
            </>
        );
    }
}
