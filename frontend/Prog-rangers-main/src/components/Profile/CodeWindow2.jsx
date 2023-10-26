// 모든 문제 풀이 코드창

import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { css } from "@emotion/react";
import axios from "axios";
import hljs from "highlight.js";
import './github-dark-dimmed.css';

export const CodeWindow2 = () => {
  
  const { solutionId } = useParams();
  const [codeData, setCodeData] = useState({ solution: { code: [] } });

  useEffect(() => {
    const apiUrl = `http://13.124.131.171:8080/api/v1/solutions/${solutionId}`;

    axios
      .get(apiUrl)
      .then((response) => {
        setCodeData(response.data);
      })
      .catch((error) => {
        console.error('API 요청 오류:', error);
      });
  }, []);

  return (
    <>
      {codeData.solution.code.map((line, lineIndex) => (
        <div key={lineIndex} css={css`padding-left: 80px; margin: 6px; `}>
          <pre css={css`font-size: 16px; white-space: pre-wrap;`} >
            <span style={{ fontFamily: 'Consolas, Courier New, monospace' }}>
              {lineIndex} {'  '}
              <span dangerouslySetInnerHTML={{ __html: hljs.highlightAuto(line).value }} />
              <br />
            </span>
          </pre>
        </div>
      ))}
    </>
  );
};
